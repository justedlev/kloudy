<div id="header" align="center">
    <h1>Kloudy</h1>
    <h3>🗃️ File storage</h3>
</div>

<div id="badges" align="center">

[![language](https://img.shields.io/badge/Java%2017-e6892e.svg?logo=openjdk&logoColor=white)](https://github.com/justedlev/kloudy)
[![framework](https://img.shields.io/badge/Spring%20Boot%203-6DB33F.svg?logo=springboot&logoColor=white)](https://docs.spring.io/spring-boot/index.html)
[![license](https://img.shields.io/github/license/justedlev/kloudy)](https://www.apache.org/licenses/LICENSE-2.0.txt)
[![stars](https://img.shields.io/github/stars/justedlev/kloudy)](https://github.com/justedlev/kloudy/star)
[![issues](https://img.shields.io/github/issues/justedlev/kloudy)](https://github.com/justedlev/kloudy/issues)

</div>

## 📋 About

Some simple file storage API

## ▶️ Run

### <a href="#"><img src="https://github.com/JetBrains/logos/raw/refs/heads/master/web/intellij-idea/intellij-idea.svg" width="16"/></a> Intellij

You can use the simple run configuration, based on [.env](/.env)
and [jvm options](/.vmoptions) to run the app locally

```xml
<component name="ProjectRunConfigurationManager">
    <configuration default="false" name="Default" type="SpringBootApplicationConfigurationType" factoryName="Spring Boot">
        <option name="ALTERNATIVE_JRE_PATH" value="17" />
        <option name="ALTERNATIVE_JRE_PATH_ENABLED" value="true" />
        <option name="envFilePaths">
            <option value="$PROJECT_DIR$/.env" />
        </option>
        <module name="kloudy" />
        <selectedOptions>
            <option name="environmentVariables" />
        </selectedOptions>
        <option name="SPRING_BOOT_MAIN_CLASS" value="io.justedlev.msrv.kloudy.KloudyApplication" />
        <option name="VM_PARAMETERS" value="@.vmoptions" />
        <method v="2">
            <option name="Make" enabled="true" />
        </method>
    </configuration>
</component>
```

### 🐳 Docker

> [!NOTE] 
> Coming soon

🗂️ Docker compose

```yaml
```
