package io.shortcut.features.space.redux

import io.shortcut.features.space.entity.PeopleInSpace
import tw.geothings.rekotlin.StateType

data class SpaceState(
        val peopleInSpace: PeopleInSpace? = null,
        val status: Status = Status.IDLE
) : StateType {

    enum class Status {
        IDLE, PENDING
    }
}
