package io.shortcut.features.space.redux

import io.shortcut.features.space.entity.PeopleInSpace
import io.shortcut.features.space.repository.SpaceRepository
import io.shortcut.network.Response
import io.shortcut.redux.Request
import io.shortcut.redux.ToastAction
import io.shortcut.redux.store
import io.shortcut.util.Strings
import tw.geothings.rekotlin.Action

class SpaceRequests {

    class FetchPeopleInSpace : Request() {

        private val spaceRepository: SpaceRepository = SpaceRepository()

        override suspend fun execute() {
            val result = when (val response = spaceRepository.getPeopleInSpace()) {
                is Response.Success -> Success(response.result)
                is Response.Failure -> Failure(Strings.get("internet_error"))
            }
            store.dispatch(result)
        }

        data class Success(val peopleInSpace: PeopleInSpace) : Action
        data class Failure(override val message: String?) : ToastAction
    }
}
