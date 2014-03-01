package frontend;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CreatedBy {
    String name() default "m.myalkin";
    String date() default "01.03.14";
}
