package frontend;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by maxim on 01.03.14.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CreatedBy {
    String name() default "m.myalkin";
    String date() default "01.03.14";
}
