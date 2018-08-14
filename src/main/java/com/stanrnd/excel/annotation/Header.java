package com.stanrnd.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.stanrnd.excel.meta.Color;
import com.stanrnd.excel.meta.FontName;
import com.stanrnd.excel.meta.FontSize;

/**
 * 
 * @author Stalin
 *
 */
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Header {

	public String value() default "";
	
	public int height() default 0;
	
	public Color foreground() default Color.BLACK;
	
	public Color background() default Color.WHITE;
	
	public FontName fontName() default FontName.ARIAL;
	
	public FontSize fontSize() default FontSize.FIVE;
	
	public boolean italic() default true;
	
	public boolean bold() default true;
	
	public boolean underline() default false;
	
}
