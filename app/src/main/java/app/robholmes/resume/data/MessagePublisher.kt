/*
 * Copyright (c) 2019. Rob Holmes. This file is part of the Resume app of Rob Holmes, an Android
 * Developer from Hampshire, United Kingdom, and is intended to demonstrate Rob's skills in an open
 * manner. It therefore should not be used for any other purpose, or any other person's Resume, as
 * this would be deceptive and misleading.
 * If you find this code anywhere other than https://github.com/robholmes/Resume then it's probably
 * a sign someone has copied it.
 */

package app.robholmes.resume.data

import app.robholmes.resume.data.model.ActionMessage
import app.robholmes.resume.data.model.Message
import app.robholmes.resume.data.model.SimpleMessage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel

class MessagePublisher {

    private val messages: Channel<Message> = Channel(Channel.UNLIMITED)

    fun messages(): ReceiveChannel<Message> = messages

    fun show(message: String) {
        messages.offer(SimpleMessage(message))
    }

    fun show(message: String, actionTitle: String, actionCallback: () -> Unit) {
        messages.offer(ActionMessage(message, actionTitle, actionCallback))
    }
}