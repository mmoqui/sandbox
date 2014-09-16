This project aims to aid us in porting of Silverpeas in JEE 7 and Java 8.
Thanks to it, we can select only the peculiar parts of Silverpeas that can drive an issue when deploying in Wildfly 8.1 and then find a way to resolve it.

This project depends upon another one in the sandbox repository, JEE7Test-Component. This project is an attempt to mock a component in Silverpeas that will be included into the final project deployment archive.
So, you will have to first build the JEE7Test-Component project before this.

Before running the integration tests, don't forget first to unset the JBOSS_HOME environment variable. The integration tests are ran either by specifying the integration-test profil or by activating it with
the profil variable set to ci.
