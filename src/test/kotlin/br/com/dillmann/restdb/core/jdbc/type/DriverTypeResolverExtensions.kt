package br.com.dillmann.restdb.core.jdbc.type

import java.lang.reflect.Field
import java.lang.reflect.Modifier

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-05-09
 */
fun DriverTypeResolver.resetLazyValue() {
    val initializerMethod = javaClass.getDeclaredMethod("resolve").also { it.isAccessible = true }
    val newValue = lazy { initializerMethod.invoke(DriverTypeResolver) as DriverType }

    javaClass.getDeclaredField("currentType\$delegate")
        .also {
            it.isAccessible = true

            val modifiersField = Field::class.java.getDeclaredField("modifiers")
            modifiersField.isAccessible = true
            modifiersField.setInt(it, it.modifiers and Modifier.FINAL.inv())
        }
        .set(DriverTypeResolver, newValue)
}