# STGG E-SPORTS APP — Android MVP v1

A native Android starter project built with Kotlin + Jetpack Compose. The app is a **local prototype**, not a production tournament platform yet.

## Included in this MVP
- Dark esports UI with red/gold highlights
- Tournament list and local registration state
- Demo leaderboard and player profile editor
- Room-info screen gated behind local registration (real credentials are not configured)
- Demo admin screen that can add a sample tournament
- Payments/notifications placeholder screens

## Important limitations
- Data stays in app memory and resets when the app process restarts; no accounts, server, cloud sync, real notifications, verified match scoring, or real room credentials.
- Payments are intentionally disabled. Never accept entry fees or promise cash prizes until you have checked applicable laws, game/publisher tournament rules, age requirements, tax/refund rules, and payment-provider terms. Since the developer may be under 18, involve a parent/guardian for accounts, contracts, payments and publishing.
- Use official game assets only if you have permission. “Free Fire” is a third-party trademark; STGG E-SPORTS is an independent fan project and is not presented as officially affiliated.
- The admin toggle is only a UI demo, not authentication or access control.

## Run in Android Studio
1. Install Android Studio (stable version) and a JDK 17-compatible setup (Android Studio's bundled JDK is usually fine).
2. Extract this ZIP to a folder.
3. In Android Studio choose **Open** and select the `STGG-ESPORTS-APP` folder (the one containing `settings.gradle.kts`).
4. Let Gradle sync. If prompted, install Android SDK Platform 35 and the Android build tools.
5. For a phone: enable Developer options + USB debugging and connect it, or create an emulator in Device Manager.
6. Select the `app` run configuration and press **Run ▶**.
7. To create a debug APK: **Build → Build Bundle(s) / APK(s) → Build APK(s)**. Android Studio will show the exact output location.

## Suggested next steps for a real release
1. Create a backend API and database; keep registration, scores and tournament status server-authoritative.
2. Add sign-in and secure player identity; add role-based admin access on the server.
3. Add push notifications (Firebase Cloud Messaging) and store device tokens securely.
4. Integrate a payment provider only after eligibility, KYC, legal and tournament-policy checks. Verify payment status on the server; never trust client-side success screens.
5. Add privacy policy, terms, consent, refund/cancellation policy, reporting and support contact.
6. Test abuse cases, data deletion, network failures, and security before Play Store release.
