# kite-api

The annotations (`@TypeName`, `@Property`, `@Cloud`), interfaces and schema model that the Kite engine
and its providers share. Provider authors get it through the provider SDK, `cloud.kitelang:kite-provider-sdk`;
[its README](https://github.com/kitecorp/kite-provider-sdk#1-add-dependency) shows the dependency to add.

## Building

`./gradlew build` builds it on its own, with JDK 25. Inside the kite superproject it is the `:kite-api` project.

## Releasing

Releases go to Maven Central as `cloud.kitelang:kite-api` through the **Publish to Maven Central** workflow
(`.github/workflows/publish-maven-central.yml`), which runs only when started by hand.

1. Raise `version` in `build.gradle`. Maven Central never accepts the same version twice.
2. Merge to `main`, then start the workflow on `main`, from the Actions tab or with
   `gh workflow run publish-maven-central.yml --repo kitecorp/kite-api --ref main`.
   It refuses to run on any other branch.
3. The run builds and tests the library and stages it in `build/staging-deploy`. JReleaser then signs every
   file, writes its checksums, checks the POM and the sources and javadoc jars against Maven Central's
   rules, uploads the bundle and waits until Central has published it. When a run fails, its
   `jreleaser-output` artifact holds JReleaser's log.
4. kite-provider-sdk depends on this library at the version its `build.gradle` names. To ship the new
   version to provider authors, raise it there and release the SDK.

The workflow reads four secrets, the same ones kite-provider-gradle-plugin publishes with, from this
repository or from the organisation:

| Secret | Value |
|---|---|
| `MAVEN_USERNAME` | Username part of a Central Portal user token (central.sonatype.com/account) |
| `MAVEN_PASSWORD` | Password part of that token |
| `GPG_PUBLIC_KEY` | `gpg --armor --export <key id>`; the key must be on a public keyserver |
| `GPG_PRIVATE_KEY` | `gpg --armor --export-secret-keys <key id>`, for a key without a passphrase |

To see what a release would upload, without publishing anything, run
`./gradlew publishAllPublicationsToStagingDeployRepository` and look in `build/staging-deploy`. JReleaser
adds the `.asc` signatures when it deploys.

## License

Apache License 2.0; see [LICENSE](LICENSE).
