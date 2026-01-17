package me.bbijabnpobatejb.waila.util;

import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.modules.i18n.I18nModule;
import lombok.experimental.UtilityClass;
import lombok.val;

@UtilityClass
public class LangUtil {

    /**
     * Retrieves a translated string based on the player's language.
     * Fallback to en-US if the specific translation is missing.
     *
     * @param key        Translation key (e.g., "block.rock_stone.name")
     * @param playerLang Player language code (e.g., "ru-RU")
     * @return Translated string or the key itself if not found
     */
    public String get(String key, String playerLang) {
        I18nModule i18n = I18nModule.get();
        if (i18n == null) return key;

        String translation = i18n.getMessage(playerLang, key);

        val en = "en-US";
        if (translation == null && !en.equals(playerLang)) {
            translation = i18n.getMessage(en, key);
        }

        return translation != null ? translation : key;
    }
    public String getTranslateKey(BlockType block) {
        val item = block.getItem();
        return item != null ? item.getTranslationKey() : "server.items." + block.getId() + ".name";
    }
}