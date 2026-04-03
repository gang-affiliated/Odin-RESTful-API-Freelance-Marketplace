param(
    [string]$Profile = "demo"
)

$ErrorActionPreference = "Stop"

$javacCommand = Get-Command javac -ErrorAction SilentlyContinue
if (-not $javacCommand) {
    Write-Error "JDK not found. Please install JDK 17+ (javac missing)."
}

$jdkBin = Split-Path $javacCommand.Source -Parent
$jdkHome = Split-Path $jdkBin -Parent
$env:JAVA_HOME = $jdkHome
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

Write-Host "Using JAVA_HOME=$env:JAVA_HOME"
Write-Host "Starting Spring Boot with profile '$Profile'..."

& ".\mvnw.cmd" "spring-boot:run" "-Dspring-boot.run.profiles=$Profile"
