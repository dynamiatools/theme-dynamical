package tools.dynamia.themes.dynamical;

import java.io.IOException;
import java.util.Properties;

import tools.dynamia.ui.icons.AbstractFontIconsProvider;
import tools.dynamia.ui.icons.Icon;
import tools.dynamia.ui.icons.IconException;
import tools.dynamia.ui.icons.InstallIcons;

@InstallIcons
public class FontAwesomeIconsProvider extends AbstractFontIconsProvider {

    private static final String FA_PREFIX = "fa-";

    @Override
    public Properties getNamesMapping() {
        try {
            Properties properties = new Properties();
            properties.load(getClass().getResourceAsStream("/icons/dynamical.properties"));
            return properties;
        } catch (IOException e) {
            throw new IconException("Unable to load dynamical theme icons", e);
        }
    }

    @Override
    public Icon getIcon(String name) {
        Icon icon = super.getIcon(name);

        if (icon == null && name.startsWith(FA_PREFIX)) {
            String internalName = name.substring(FA_PREFIX.length());
            icon = newIcon(name, internalName);
        }

        return icon;
    }

    @Override
    protected Icon newIcon(String name, String internalName) {
        return new FAIcon(name, "fa " + FA_PREFIX + internalName);
    }
}
