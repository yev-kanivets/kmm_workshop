package io.shortcut.features.space.redux

import io.shortcut.features.space.entity.PeopleInSpace
import io.shortcut.redux.AppState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class SpaceReducerTest {

    @Test
    fun testFetchPeopleInSpaceSuccess() {
        val expected = SpaceState(PeopleInSpace(42), SpaceState.Status.IDLE)
        val actual = spaceReducer(SpaceRequests.FetchPeopleInSpace.Success(PeopleInSpace(42)), AppState())
        assertEquals(expected, actual)
    }
}
