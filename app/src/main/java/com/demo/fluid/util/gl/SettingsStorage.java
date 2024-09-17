package com.demo.fluid.util.gl;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.Uri;
import android.util.Log;
import androidx.core.view.ViewCompat;
import com.magicfluids.Config;
import com.magicfluids.ConfigID;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class SettingsStorage {
    private static final String PRESET_EXISTS = "PRESET_EXISTS";
    private static final String PRESET_NAME = "PRESET_NAME";
    public static final String SETTINGS_LWP_NAME = "LWPSettings";
    public static final String SETTINGS_NAME = "Settings";

    public interface SettingsMap {
        boolean getBoolean(String str, boolean z);

        float getFloat(String str, float f);

        int getInt(String str, int i);
    }

    static String loadTextFileFromAssets(AssetManager assetManager, String str) {
        try {
            InputStream open = assetManager.open(str);
            byte[] bArr = new byte[open.available()];
            open.read(bArr);
            open.close();
            Log.d("Load Config", "loadTextFileFromAssets: ".concat(new String(bArr)));
            return new String(bArr);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static class SettingsFileMap implements SettingsMap {
        Map<String, String> map = new HashMap();

        public SettingsFileMap(String str) {
            String[] split = str.split("\\s+");
            if (split.length % 2 != 0) {
                Log.e("SettingsFileMap", "Settings file has incorrect format: " + str);
                return;
            }
            for (int i = 0; i < split.length; i += 2) {
                this.map.put(split[i], split[i + 1]);
            }
        }

        @Override
        public float getFloat(String str, float f) {
            String str2 = this.map.get(str);
            return str2 != null ? Float.valueOf(str2).floatValue() : f;
        }

        @Override
        public int getInt(String str, int i) {
            String str2 = this.map.get(str);
            return str2 != null ? Integer.valueOf(str2).intValue() : i;
        }

        @Override
        public boolean getBoolean(String str, boolean z) {
            String str2 = this.map.get(str);
            return str2 != null ? Boolean.valueOf(str2).booleanValue() : z;
        }
    }

    public static class SharedPrefsMap implements SettingsMap {
        SharedPreferences prefs;

        public SharedPrefsMap(SharedPreferences sharedPreferences) {
            this.prefs = sharedPreferences;
        }

        @Override
        public float getFloat(String str, float f) {
            return this.prefs.getFloat(str, f);
        }

        @Override
        public int getInt(String str, int i) {
            return this.prefs.getInt(str, i);
        }

        @Override
        public boolean getBoolean(String str, boolean z) {
            return this.prefs.getBoolean(str, z);
        }
    }

    private static byte[] sharedPrefsToByteArray(SharedPreferences sharedPreferences) {
        String str = "";
        for (Map.Entry next : sharedPreferences.getAll().entrySet()) {
            if (!((String) next.getKey()).equals(PRESET_NAME) && !((String) next.getKey()).equals(PRESET_EXISTS)) {
                str = str + ((String) next.getKey()) + " " + next.getValue().toString() + " ";
            }
        }
        return str.getBytes();
    }

    private static void saveConfig(Context context, Config config, String str) {
        SharedPreferences.Editor edit = context.getSharedPreferences(str, 0).edit();
        for (Map.Entry next : config.getMap().entrySet()) {
            if (((Config.ConfigVal) next.getValue()).Type == Config.ConfigVal.DataType.FLOAT) {
                edit.putFloat(((ConfigID) next.getKey()).name(), ((Config.FloatVal) next.getValue()).Value);
            }
            if (((Config.ConfigVal) next.getValue()).Type == Config.ConfigVal.DataType.INT) {
                edit.putInt(((ConfigID) next.getKey()).name(), ((Config.IntVal) next.getValue()).Value);
            }
            if (((Config.ConfigVal) next.getValue()).Type == Config.ConfigVal.DataType.BOOL) {
                edit.putBoolean(((ConfigID) next.getKey()).name(), ((Config.BoolVal) next.getValue()).Value);
            }
        }
        edit.apply();
    }

    public static boolean prefFileExist(Context context, String str) {
        return context.getSharedPreferences(str, 0).contains(ConfigID.FLUID_TYPE.name());
    }

    private static void loadConfigFromMap(SettingsMap settingsMap, Config config, boolean z) {
        for (Map.Entry next : config.getMap().entrySet()) {
            if (z || ((Config.ConfigVal) next.getValue()).IsPresetRelated) {
                if (((Config.ConfigVal) next.getValue()).Type == Config.ConfigVal.DataType.FLOAT) {
                    Config.FloatVal floatVal = (Config.FloatVal) next.getValue();
                    floatVal.Value = settingsMap.getFloat(((ConfigID) next.getKey()).name(), floatVal.Default);
                }
                if (((Config.ConfigVal) next.getValue()).Type == Config.ConfigVal.DataType.INT) {
                    Config.IntVal intVal = (Config.IntVal) next.getValue();
                    intVal.Value = settingsMap.getInt(((ConfigID) next.getKey()).name(), intVal.Default);
                    if (intVal.IsColor) {
                        intVal.Value |= ViewCompat.MEASURED_STATE_MASK;
                    }
                }
                if (((Config.ConfigVal) next.getValue()).Type == Config.ConfigVal.DataType.BOOL) {
                    Config.BoolVal boolVal = (Config.BoolVal) next.getValue();
                    boolVal.Value = settingsMap.getBoolean(((ConfigID) next.getKey()).name(), boolVal.Default);
                }
            }
        }
    }

    private static void loadConfigFromSharedPrefs(Context context, Config config, String str, boolean z) {
        loadConfigFromMap(new SharedPrefsMap(context.getSharedPreferences(str, 0)), config, z);
    }

    private static String getUserPresetPrefKey(int i) {
        return "UserSettings" + i;
    }

    public static void loadSessionConfig(Context context, Config config, String str) {
        loadConfigFromSharedPrefs(context, config, str, true);
    }

    public static void saveSessionConfig(Context context, Config config, String str) {
        saveConfig(context, config, str);
    }

    public static void loadConfigFromUserPreset(Context context, Config config, int i) {
        loadConfigFromSharedPrefs(context, config, getUserPresetPrefKey(i), false);
    }

    public static void saveConfigToUserPreset(Context context, Config config, int i, String str) {
        String userPresetPrefKey = getUserPresetPrefKey(i);
        saveConfig(context, config, userPresetPrefKey);
        SharedPreferences.Editor edit = context.getSharedPreferences(userPresetPrefKey, 0).edit();
        edit.putString(PRESET_NAME, str);
        edit.putBoolean(PRESET_EXISTS, true);
        edit.commit();
    }

    public static void removeUserPreset(Context context, int i) {
        SharedPreferences.Editor edit = context.getSharedPreferences(getUserPresetPrefKey(i), 0).edit();
        edit.putString(PRESET_NAME, "Empty");
        edit.putBoolean(PRESET_EXISTS, false);
        edit.commit();
    }

    public static void loadConfigFromInternalPreset(String str, AssetManager assetManager, Config config) {
        loadConfigFromMap(new SettingsFileMap(loadTextFileFromAssets(assetManager, "presets/" + str.replaceAll("\\s+", ""))), config, false);
    }

    public static String getUserPresetName(Context context, int i) {
        return context.getSharedPreferences("UserSettings" + i, 0).getString(PRESET_NAME, "Empty");
    }

    public static boolean isSavedUserPreset(Context context, int i) {
        return context.getSharedPreferences("UserSettings" + i, 0).getBoolean(PRESET_EXISTS, false);
    }

    public static boolean exportPresets(Context context, Uri uri) {
        try {
            OutputStream openOutputStream = context.getContentResolver().openOutputStream(uri);
            try {
                openOutputStream.write("mf_presetfile_v01".getBytes());
                openOutputStream.write("\n".getBytes());
                for (int i = 0; i < 20; i++) {
                    if (isSavedUserPreset(context, i)) {
                        openOutputStream.write(Integer.toString(i).getBytes());
                        openOutputStream.write(" ".getBytes());
                        openOutputStream.write(getUserPresetName(context, i).replace(' ', '_').getBytes());
                        openOutputStream.write(" ".getBytes());
                        openOutputStream.write(sharedPrefsToByteArray(context.getSharedPreferences(getUserPresetPrefKey(i), 0)));
                        openOutputStream.write("\n".getBytes());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                openOutputStream.flush();
                openOutputStream.close();
                return true;
            } catch (IOException e2) {
                e2.printStackTrace();
                return false;
            }
        } catch (FileNotFoundException e3) {
            e3.printStackTrace();
            return false;
        }
    }
}
