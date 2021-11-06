package fr.alkanife.amiria;

import fr.alkanife.amiria.characters.Characters;
import fr.alkanife.amiria.commands.CommandHandler;

public class StatusReport {

    private String status = "";

    public StatusReport() {}

    public StatusReport(boolean checkAll) {
        version();
        upday();
        newLine();
        checkTranslations();
        checkCharacters();
        checkCommands();
    }

    public StatusReport newLine() {
        status += "\n";
        return this;
    }

    public StatusReport version() {
        status += "[Amiria version " + Amiria.getVersion() + "]\n";
        return this;
    }

    public StatusReport upday() {
        status += "[Updays : " + Utils.getUpDays() + "]\n";
        return this;
    }

    public StatusReport checkTranslations() {
        return checkTranslations(false);
    }

    public StatusReport checkTranslations(boolean errorOverride) {
        if (errorOverride) {
            status += " 🛑 Erreur lors du chargement des traductions\n";
            return this;
        }

        if (Lang.getTranslations().size() == 0) {
            status += " ⚠️ Aucune traduction n'a été trouvé\n";
        } else {
            status += " ✅ `" + Lang.getTranslations().size() + "` tradcutions ont été chargées\n";
        }

        return this;
    }

    public StatusReport checkCharacters() {
        return checkCharacters(false);
    }

    public StatusReport checkCharacters(boolean errorOverride) {
        if (errorOverride) {
            status += " 🛑 Erreur lors du chargement des personnages\n";
            return this;
        }

        if (Characters.getCharacters().size() == 0) {
            status += " ⚠️ Aucun personnage n'a été chargé\n";
        } else {
            if (Characters.getFailed().size() == 0) {
                status += " ✅ Tous les `" + Characters.getCharacters().size() + "` personnages présents ont été chargés\n";
            } else {
                status += " ✅ `" + Characters.getCharacters().size() + "` personnages ont été chargés    **MAIS**\n";

                if (Characters.getFailed().size() > 10) {
                    status += " ⚠️ `" + Characters.getFailed().size() + "` personnages n'ont pas été chargés\n";
                } else {
                    for (String c : Characters.getFailed())
                        status += " ⚠️ Le personnage `" + c + "` n'a pas été chargé\n";
                }
            }
        }

        return this;
    }

    public StatusReport checkCommands() {
        if (CommandHandler.getCommands().size() == 0) {
            Amiria.getLogger().warn("No commands loaded");
            status += " ⚠️ Aucune commande n'a été chargé\n";
        } else {
            status += " ✅ `" + CommandHandler.getCommands().size() + "` commandes ont été chargés\n";
        }

        return this;
    }

    public String getStatus() {
        return status;
    }
}
