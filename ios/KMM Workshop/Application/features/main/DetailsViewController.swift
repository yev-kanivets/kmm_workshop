import UIKit
import SwiftUI
import common

class DetailsViewController: UIHostingController<DetailsView>, ReKampStoreSubscriber {
    
    private let issueNumber: Int

    init(issueNumber: Int) {
        self.issueNumber = issueNumber
        super.init(rootView: DetailsView())
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

    func onNewState(state: Any) {
        let state = state as! GitHubState
        
        guard let issue = state.issues.first(where: { $0.number == issueNumber }) else { return }
 
        rootView.uiModel = .init(number: Int(issue.number), title: issue.title, body: issue.body)
    }
}

struct DetailsView: View {
    
    class UiModel: ObservableObject {
        
        init(
            number: Int,
            title: String,
            body: String
        ) {
            self.number = number
            self.title = title
            self.body = body
        }
        
        var number: Int
        var title: String
        var body: String
    }

    @ObservedObject var uiModel: UiModel = .init(number: -1, title: "", body: "")
    
    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            TextField("Title", text: $uiModel.title)
                .font(.headline)
                .background(Color.gray)
            
            TextField("Body", text: $uiModel.body)
                .font(.body)
                .background(Color.gray)
            
            Button(action: {
                store.dispatch(action: GitHubRequests.UpdateIssue(
                    issueNumber: Int32(uiModel.number),
                    title: uiModel.title,
                    body: uiModel.body
                ))
            }, label: {
                Text("Save")
            })
            
            Spacer()
        }
        .padding(16)
    }
}
