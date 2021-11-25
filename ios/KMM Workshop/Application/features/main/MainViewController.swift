import UIKit
import SwiftUI
import common

class MainViewController: UIHostingController<MainView>, ReKampStoreSubscriber {

    init() {
        super.init(rootView: MainView())
    }
    
    @objc required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)

        store.subscribe(subscriber: self) { subscription in
            subscription.skipRepeats { oldState, newState in
                return KotlinBoolean(bool: oldState.gitHub == newState.gitHub)
            }.select { state in
                return state.gitHub
            }
        }
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        store.unsubscribe(subscriber: self)
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        store.dispatch(action: GitHubRequests.FetchIssues())
    }
    
    func onNewState(state: Any) {
        let state = state as! GitHubState

        switch(state.status) {
        case .idle:
            rootView.peoplesInSpace = "\(state.issues.count)"
        case .pending:
            rootView.peoplesInSpace = "Loading ..."
        default:
            break
        }
    }
}

struct MainView: View {

    var peoplesInSpace = ""
    
    var body: some View {
        Text(peoplesInSpace)
    }
}
