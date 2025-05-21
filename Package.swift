// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "JavaPlugin",
    platforms: [.iOS(.v13)],
    products: [
        .library(
            name: "JavaPlugin",
            targets: ["javapluginPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", branch: "main")
    ],
    targets: [
        .target(
            name: "javapluginPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/javapluginPlugin"),
        .testTarget(
            name: "javapluginPluginTests",
            dependencies: ["javapluginPlugin"],
            path: "ios/Tests/javapluginPluginTests")
    ]
)