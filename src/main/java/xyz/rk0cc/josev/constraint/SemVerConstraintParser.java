package xyz.rk0cc.josev.constraint;

import javax.annotation.Nonnull;
import java.lang.annotation.*;

/**
 * An {@link Annotation} that allowing a subclass of {@link SemVerConstraint} can be parsed by
 * {@link SemVerConstraint#parse(Class, String)}.
 *
 * @since 1.0.0
 * 
 * @see SemVerConstraint#parse(Class, String)
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SemVerConstraintParser {
    /**
     * Allow annotated class can be parsed by {@link SemVerConstraint#parse(Class, String)}.
     *
     * @return <code>true</code> if enable.
     */
    boolean enabled() default true;

    /**
     * Define which <b>public static method</b> of annotated class will be used when applying
     * {@link SemVerConstraint#parse(Class, String)}.
     * <br/>
     * The parser must return the same type of the located class with a {@link String} as parameter.
     *
     * @return Method name of the parser, default uses <code>"parse"</code>.
     */
    @Nonnull
    String parserMethodName() default "parse";
}
