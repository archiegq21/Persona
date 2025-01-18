import SwiftUI
import shared

struct PersonaApp: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        AppViewControllerKt.AppViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}


struct ContentView: View {

	var body: some View {
        PersonaApp()
            .ignoresSafeArea(.all)
	}
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
