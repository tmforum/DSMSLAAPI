/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tmf.org.dsmapi.commons.jaxrs;



/**
 *
 * @author pierregauthier
 */
import javax.ws.rs.HttpMethod;
import java.lang.annotation.*;

/**
 *
 * @author maig7313
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@HttpMethod("PATCH")
public @interface PATCH
{
}

