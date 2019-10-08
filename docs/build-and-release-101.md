# Build
The `gytheio` project uses _Travis CI_. \
The `.travis.yml` config file can be found in the root of the repository.


## Stages and Jobs
1. **Build**:  Java Build with Unit Tests, WhiteSource
3. **Release**: Publish to Nexus


## Branches
Travis CI builds differ by branch:
* `master`:
  - regular builds which include only the _Build_ stage;
* `MM-*` (any branch starting with "MM-"):
  - regular builds which include only the _Build_ stage;
* `SP/*`
  - regular builds which include only the _Build_ stage; 
* `release/SP/*`
  - builds that include the _Build_ and _Release_ stages;
  - PR builds with `release` as the target branch only execute dry runs of the actual release, 
    without actually publishing anything;
* `release`:
  - builds that include the _Build_ and _Release_ stages;
  - PR builds with `release` as the target branch only execute dry runs of the actual release, 
  without actually publishing anything;


All other branches are ignored.


## Release process steps & info
Prerequisites:
 - the `master` / `SP/*` branch has a green build and it contains all the changes that should be included in
  the next release

Steps:
1. Create a new branch from the `master` / `SP/*` branch with the name `MM-###_release_version`;
2. (Optional) Update the project version if the current POM version is not the next desired
 release; use a maven command, i.e. `mvn versions:set -DnewVersion=0.11.N-SNAPSHOT versions:commit`;
3. Update the project's dependencies (remove the `-SNAPSHOT` suffixes) through a new commit on the
 `MM-###_release_version` branch;
4. Open a new Pull Request from the `MM-###_release_version` branch into the `release` / `release/SP/*` branch and
 wait for a green build; the **Release** stage on the PR build will only execute a _Dry_Run_ of
  the release;
5. Once it is approved, merge the PR through the **Create a merge commit** option (as opposed to
 _Squash and merge_ or _Rebase and merge_), delete the `MM-###_release_version` branch, and wait 
 for a green build on the `release` branch;
6. Merge back the release branch into the initial branch;
7. Update the project dependencies (append the `-SNAPSHOT` suffixes)

Steps (6) and (7) can be done either directly from an IDE or through the GitHub flow, by creating
another branch and PR. Just make sure you don't add extra commits directly to the release branch,
as this will trigger another release.


