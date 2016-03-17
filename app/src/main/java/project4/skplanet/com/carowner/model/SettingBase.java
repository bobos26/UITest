package project4.skplanet.com.carowner.model;

import android.text.TextUtils;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.TableInfo;
import com.activeandroid.query.Select;
import com.activeandroid.util.SQLiteUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class SettingBase extends Model {
    public static final String TAG = SettingBase.class.getSimpleName();
    public static final boolean DEBUG = true;
    private static final String FIELD_PREFIX = "FIELD_";

    private HashMap<String, Field> mHashMap = new HashMap<String, Field>();

    protected abstract String onEncrypt(String rawData);

    protected abstract String onDecrypt(String encData);


    private Field getField(String fieldName, boolean doCache) {
        Field field = null;
        if (doCache) {
            field = _hashGet(fieldName);
        }

        if (field == null) {
            try {
                field = getClass().getDeclaredField(fieldName);
                if (field.getType() != String.class) {
                    throw new IllegalAccessException(fieldName + " field SHOULD BE String Type.");
                }
                if (doCache) {
                    _hashPut(fieldName, field);
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }

        return field;
    }

    public void setValue(String fieldName, String value) {
        _setValue(fieldName, value, true, true);
    }

    public String getValue(String fieldName) {
        return _getValue(fieldName, true, true);
    }

    // 기본적으로 DB는 모두 String 이지만 
    // 설정 on/off 값은 하기 함수를 호출한다.
    public void setValueAsBoolean(String field, boolean b) {
        String value = Boolean.toString(b);
        setValue(field, value);
    }

    // 기본적으로 DB는 모두 String 이지만 
    // 설정 on/off 값은 하기 함수를 호출한다.
    public boolean getValueAsBoolean(String field) {
        return getValueAsBoolean(field, false);
    }

    public boolean getValueAsBoolean(String field, boolean defaultValue) {
        boolean ret = defaultValue;

        String value = getValue(field);

        try {
            if (!TextUtils.isEmpty(value)) {
                ret = Boolean.parseBoolean(value);
            }
        } catch (Exception e) {
        }

        return ret;
    }

    public void setValueAsInt(String field, int i) {
        String value = Integer.toString(i);
        setValue(field, value);
    }

    public int getValueAsInt(String field) {
        int ret = -1;
        String value = getValue(field);

        try {
            if (!TextUtils.isEmpty(value)) {
                ret = Integer.parseInt(value);
            }
        } catch (Exception e) {
        }
        return ret;
    }

    public void removeField(String field) {
        _setValue(field, "", false, true);
    }

    private Field _hashGet(String key) {
        return mHashMap.get(key);
    }

    private void _hashPut(String key, Field field) {
        mHashMap.put(key, field);
    }

    private void _setValue(String fieldName, String value, boolean doCrypto, boolean doHash) {
        if (TextUtils.isEmpty(fieldName)) {
            return;
        }

        String enValue = (doCrypto) ? (onEncrypt(value)) : (value);

        try {
            Field field = getField(fieldName, doHash);
            if (field != null) {
                field.setAccessible(true);
                field.set(this, enValue);
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    private String _getValue(String fieldName, boolean doCrypto, boolean doCache) {
        String ret = null;
        if (TextUtils.isEmpty(fieldName)) {
            return ret;
        }

        try {
            Field field = getField(fieldName, doCache);
            if (field != null) {
                field.setAccessible(true);
                String encValue = (String) field.get(this);
                if (!TextUtils.isEmpty(encValue)) {
                    ret = (doCrypto) ? (onDecrypt(encValue)) : (encValue);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return ret;
    }

    @SuppressWarnings("unchecked")
    protected static <T extends Model> T getInstance(
            Class<? extends Model> table) {
        List<Model> items = getAll(table);
        T ret = null;

        if (items != null && items.size() > 0) {
            ret = (T) items.get(0);
            return ret;
        }

        try {
            ret = (T) table.newInstance();
            // FIXME only for development cycle
            ret.save();

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return ret;
    }

    // FIXME only for development cycle
    public static void recreateData(Class<? extends Model> model) {
        Log.e(TAG, " " + model.getSimpleName() + " table redefinition");

        TableInfo table = Cache.getTableInfo(model);
        if (table == null) {
            Log.e(TAG, "wranning: doesn't have table info");
            return;
        }

        ActiveAndroid.beginTransaction();
        ActiveAndroid.execSQL("DROP TABLE IF EXISTS " + table.getTableName());
        ActiveAndroid.execSQL(SQLiteUtils.createTableDefinition(table));
        ActiveAndroid.endTransaction();
    }

    public static <T extends Model> List<T> getAll(
            Class<? extends Model> table) {
        return new Select().from(table).execute();
    }

    private List<Field> _getFields(Class<?> clazz, String filter) {
        List<Field> privateFields = new ArrayList<Field>();

        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (Modifier.isPublic(field.getModifiers()) &&
                    Modifier.isStatic(field.getModifiers()) &&
                    field.getName().startsWith(filter)) {
                privateFields.add(field);
            }
        }

        return privateFields;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        List<Field> fields = _getFields(getClass(), FIELD_PREFIX);
        for (Field field : fields) {
            String key = _getValue(field.getName(), false, false);
            String value = getValue(key);
            sb.append(key + " : " + value + ", ");
        }

        return sb.toString();
    }
}
