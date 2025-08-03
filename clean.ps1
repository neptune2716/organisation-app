# Script de nettoyage pour le projet Android
# Utilisez ce script pour nettoyer les fichiers temporaires avant un commit Git

Write-Host "🧹 Nettoyage du projet Android..." -ForegroundColor Yellow

# Arrêter les processus Gradle
Write-Host "⏹️  Arrêt des processus Gradle..." -ForegroundColor Blue
Get-Process | Where-Object {$_.Name -like "*gradle*" -or $_.Name -like "*java*"} | Stop-Process -Force -ErrorAction SilentlyContinue

# Nettoyer les dossiers de build
Write-Host "🗑️  Suppression des dossiers build..." -ForegroundColor Blue
Remove-Item "build" -Recurse -Force -ErrorAction SilentlyContinue
Remove-Item "app\build" -Recurse -Force -ErrorAction SilentlyContinue
Remove-Item ".gradle" -Recurse -Force -ErrorAction SilentlyContinue

# Nettoyer les fichiers temporaires
Write-Host "🗑️  Suppression des fichiers temporaires..." -ForegroundColor Blue
Remove-Item "gradle-temp" -Recurse -Force -ErrorAction SilentlyContinue
Remove-Item "gradle-*.zip" -Force -ErrorAction SilentlyContinue
Remove-Item "*.tmp" -Force -ErrorAction SilentlyContinue

# Nettoyer le cache Gradle utilisateur (optionnel)
$choice = Read-Host "Voulez-vous nettoyer le cache Gradle utilisateur ? (y/n)"
if ($choice -eq "y" -or $choice -eq "Y") {
    Write-Host "🗑️  Nettoyage du cache Gradle..." -ForegroundColor Blue
    Remove-Item "$env:USERPROFILE\.gradle\caches" -Recurse -Force -ErrorAction SilentlyContinue
}

Write-Host "✅ Nettoyage terminé ! Prêt pour Git." -ForegroundColor Green
Write-Host "💡 Vous pouvez maintenant faire : git add . && git commit" -ForegroundColor Cyan
