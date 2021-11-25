package io.shortcut.android.features

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import io.shortcut.features.github.entity.Issue
import io.shortcut.features.github.redux.GitHubRequests
import io.shortcut.features.github.redux.GitHubState
import io.shortcut.redux.store
import tw.geothings.rekotlin.StoreSubscriber

class MainActivity : ComponentActivity(), StoreSubscriber<GitHubState> {

    private val spaceState = MutableLiveData<GitHubState>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainView()
        }

        store.dispatch(
            GitHubRequests.FetchIssues()
        )
    }

    override fun onNewState(state: GitHubState) {
        spaceState.postValue(state)
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
        val state = spaceState.observeAsState().value ?: return

        Box(
            modifier = modifier.fillMaxSize()
        ) {
            when (state.status) {
                GitHubState.Status.IDLE -> IssuesList(issues = state.issues)
                GitHubState.Status.PENDING -> Loading()
            }
        }
    }

    @Composable
    private fun IssuesList(issues: List<Issue>) = LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(issues) { issue ->
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = "#${issue.number}: ${issue.title}",
                        style = MaterialTheme.typography.h6
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = issue.body, style = MaterialTheme.typography.body1)
                }
            }
        }
    }

    @Composable
    private fun Loading() = Text(
        text = "Loading ..."
    )
}