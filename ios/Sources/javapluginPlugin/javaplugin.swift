import Foundation

@objc public class javaplugin: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
