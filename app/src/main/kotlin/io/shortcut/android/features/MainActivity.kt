package io.shortcut.android.features

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.MutableLiveData
import io.shortcut.features.github.redux.GitHubRequests
import io.shortcut.features.github.redux.GitHubState
import io.shortcut.redux.store
import tw.geothings.rekotlin.StoreSubscriber

class MainActivity : ComponentActivity(), StoreSubscriber<GitHubState> {

    private val gitHubState = MutableLiveData<GitHubState>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainView()
        }

        store.dispatch(GitHubRequests.FetchIssues())
    }

    override fun onNewState(state: GitHubState) {
        gitHubState.postValue(state)
    }

    override fun onStart() {
        super.onStart()

        store.subscribe(this) { state ->
            state.skipRepeats { oldState, newState ->
                oldState.gitHub == newState.gitHub
            }.select { it.gitHub }
        }
    }

    override fun onStop() {
        super.onStop()
        store.unsubscribe(this)
    }

    @Composable
    private fun MainView(
        modifier: Modifier = Modifier
    ) {
        val state = gitHubState.observeAsState().value ?: return

        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = when (state.status) {
                    GitHubState.Status.IDLE -> state.issues
                    GitHubState.Status.PENDING -> "Loading ..."
                }
            )
        }
    }
}
