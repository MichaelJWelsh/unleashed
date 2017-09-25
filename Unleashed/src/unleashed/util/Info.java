package unleashed.util;

/* This is an annotation that helps clarify and
 * organize data information for our group members.
 * 
 * IT IS MANDATORY that whenever code is written/edited,
 * this annotation is declared at the top of the class of which the code is written/edited in.
 * 
 * The variables "dateLastEdited" and "purpose" (which describes the overall functionality/purpose of the class)
 * must be filled out.  The variable "otherInfo" can be filled out to provide additional off-topic information
 */

public @interface Info {
	String dateLastEdited();
	String purpose();
	String otherInfo() default "";
}
