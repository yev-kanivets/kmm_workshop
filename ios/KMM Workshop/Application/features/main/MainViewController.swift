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
        
        title = "KMM Workshop"
        rootView.onTap = { [weak self] issueNumber in
            let detailsViewController = DetailsViewController(issueNumber: issueNumber)
            self?.navigationController?.pushViewController(detailsViewController, animated: true)
        }
        
        store.dispatch(action: GitHubRequests.FetchIssues())
    }
    
    func onNewState(state: Any) {
        let state = state as! GitHubState

        switch(state.status) {
        case .idle:
            rootView.issues = state.issues
        case .pending:
            break
            // rootView.issues = "Loading ..."
        default:
            break
        }
    }
}

struct MainView: View {

    var issues: [Issue] = []
    var onTap: (Int) -> () = { _ in }
    
    var body: some View {
        if issues.isEmpty {
            Text("Loading ...")
        } else {
            ScrollView {
                VStack(spacing: 8) {
                    ForEach(issues, id: \.number) { issue in
                        VStack(alignment: .leading, spacing: 4) {
                            Text("#\(issue.number): \(issue.title)")
                                .font(.headline)
                            
                            Text(issue.body)
                                .font(.body)
                        }
                        .padding(8)
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .background(Color.white)
                        .cornerRadius(4)
                        .shadow(radius: 10)
                        .onTapGesture { onTap(Int(issue.number)) }
                    }
                }
                .padding(.vertical, 8)
                .padding(.horizontal, 16)
            }
        }
    }
}
