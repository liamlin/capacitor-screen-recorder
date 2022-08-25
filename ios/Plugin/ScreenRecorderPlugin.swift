import Foundation
import Capacitor

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(ScreenRecorderPlugin)
public class ScreenRecorderPlugin: CAPPlugin {
    private let implementation = ScreenRecorder()

    @objc func start(_ call: CAPPluginCall) {
        let mute = call.getBool("mute") ?? true
        let saveToCameraRoll = call.getBool("saveToCameraRoll") ?? false
        implementation.startRecording(saveToCameraRoll: saveToCameraRoll, mute: mute, handler: { error in
            if let error = error {
                debugPrint("Error when start recording \(error)")
                call.reject("Cannot start recording")
            } else {
                call.resolve()
            }
        })
    }
    @objc func stop(_ call: CAPPluginCall) {
        implementation.stoprecording(handler: { (error, url) in
            if let error = error {
                debugPrint("Error when stop recording \(error)")
                call.reject("Cannot stop recording")
            } else if let url = url {
                debugPrint("got url when stop recording \(url)")
                call.resolve(["url": url.absoluteString])
            }
        })
    }
}
