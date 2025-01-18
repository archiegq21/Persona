import Foundation
import UIKit
import FirebaseCore
import shared

class AppDelegate: NSObject, UIApplicationDelegate {
  func application(_ application: UIApplication,
                   didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
    KoinApplication_iosKt.doInitKoin()
    FirebaseApp.configure()
    CrashlyticsStartUp_iosKt.setUpCrashlytics()
    return true
  }
}
