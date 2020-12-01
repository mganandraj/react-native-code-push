npm install -g react-native-cli
npm install -g appcenter-cli
npm install

appcenter login

appcenter orgs create -n PizzaTranslatorOrg -d PizzaTranslatorOrg
appcenter orgs apps create -n PizzaTranslatorOrg -p React-Native -o Android -a PizzaTranslator -d PizzaTranslator

appcenter codepush deployment add -a 'PizzaTranslatorOrg/PizzaTranslator' TestDeployment

# To push a release
npx react-native bundle --entry-file index.js --bundle-output index.android.bundle --dev true --platform android
appcenter codepush release -c index.android.bundle -t 1.0 -a PizzaTranslatorOrg/PizzaTranslator -d TestDeployment

# To package the seed bundle with the application
npx react-native bundle --entry-file index.js --bundle-output index.android.bundle --dev true --platform android
copy E:\github\simple-original\index.android.bundle C:\Office\dev\officemobile\android\Java\assets\index.android.bundle
node E:\github\simple-original\node_modules\react-native-code-push\scripts\generateBundledResourcesHash.js C:\Office\dev\officemobile\android\Java\res C:\Office\dev\officemobile\android\Java\assets\index.android.bundle C:\Office\dev\officemobile\android\Java\assets

