package io.shortcut.android.features

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import io.shortcut.features.github.entity.Issue
import io.shortcut.features.github.redux.GitHubRequests
import io.shortcut.features.github.redux.GitHubState
import io.shortcut.redux.store
import tw.geothings.rekotlin.StoreSubscriber

class DetailsActivity: ComponentActivity(), StoreSubscriber<GitHubState> {

    private val issueState = MutableLiveData<Issue?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DetailsView()
        }
    }

    override fun onNewState(state: GitHubState) {
        val issueNumber = intent.getIntExtra(KEY_ISSUE_NUMBER, -1)
        val issue = when (state.status) {
            GitHubState.Status.IDLE -> state.issues.firstOrNull { it.number == issueNumber }
            GitHubState.Status.PENDING -> null
        }
        issueState.postValue(issue)
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

    private fun updateIssue(
        issueNumber: Int,
        title: String,
        body: String
    ) = store.dispatch(GitHubRequests.UpdateIssue(issueNumber, title, body))

    @Composable
    private fun DetailsView(
        modifier: Modifier = Modifier
    ) {
        val issue = issueState.observeAsState().value ?: return

        var title by remember { mutableStateOf(issue.title) }
        var body by remember { mutableStateOf(issue.body) }

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = body,
                onValueChange = { body = it },
                modifier = Modifier.fillMaxWidth()
            )
            Button(onClick = {
                updateIssue(issue.number, title, body)
            }) {
                Text("Save")
            }
        }
    }

    companion object {

        fun createIntent(
            context: Context,
            issueNumber: Int
        ) = Intent(context, DetailsActivity::class.java).apply {
            putExtra(KEY_ISSUE_NUMBER, issueNumber)
        }

        private const val KEY_ISSUE_NUMBER = "issue_number"
    }
}