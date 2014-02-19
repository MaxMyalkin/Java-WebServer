package pageGenerator;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

/**
 * Created by maxim on 15.02.14.
 */

public class PageGenerator {
    private static final String TML_DIR = "tml";
    private static final Configuration CFG = new Configuration(); // конфигурация freemaker

    public static String getPage(String filename, Map<String, Object> data) {
        Writer stream = new StringWriter();
        try {
            CFG.setDirectoryForTemplateLoading(new File(TML_DIR));//Установка папки с шаблонами
            CFG.setDefaultEncoding("UTF-8");
            Template template = CFG.getTemplate(filename);
            template.process(data, stream); // обработка шаблона с данными
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
        return stream.toString();
    }
}