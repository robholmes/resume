/*
 * Copyright (c) 2019. Rob Holmes. This file is part of the Resume app of Rob Holmes, an Android
 * Developer from Hampshire, United Kingdom, and is intended to demonstrate Rob's skills in an open
 * manner. It therefore should not be used for any other purpose, or any other person's Resume, as
 * this would be deceptive and misleading.
 * If you find this code anywhere other than https://github.com/robholmes/Resume then it's probably
 * a sign someone has copied it.
 */

package app.robholmes.resume.data.serializers

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.SerializationException
import kotlinx.serialization.Serializer
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.withName
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter.ISO_LOCAL_DATE

@Serializer(forClass = LocalDate::class)
object LocalDateSerializer : KSerializer<LocalDate> {

    override val descriptor: SerialDescriptor =
        StringDescriptor.withName("LocalDate")

    override fun serialize(encoder: Encoder, obj: LocalDate) {
        encoder.encodeString(obj.format(ISO_LOCAL_DATE))
    }

    override fun deserialize(decoder: Decoder): LocalDate {
        return try {
            LocalDate.parse(decoder.decodeString(), ISO_LOCAL_DATE)
        } catch (e: Exception) {
            throw SerializationException(e.message ?: "Unknown exception", e)
        }
    }
}