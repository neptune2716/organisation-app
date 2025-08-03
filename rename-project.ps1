# Script pour renommer le répertoire du projet
# Exécutez ce script depuis le répertoire parent

param(
    [string]$OldName = "mobile",
    [string]$NewName = "organisation-app"
)

Write-Host "📁 Renommage du répertoire du projet..." -ForegroundColor Yellow
Write-Host "De: $OldName → Vers: $NewName" -ForegroundColor Cyan

if (Test-Path $OldName) {
    if (Test-Path $NewName) {
        Write-Host "❌ Le répertoire '$NewName' existe déjà !" -ForegroundColor Red
        exit 1
    }
    
    Write-Host "🔄 Renommage en cours..." -ForegroundColor Blue
    Rename-Item $OldName $NewName
    
    Write-Host "✅ Projet renommé avec succès !" -ForegroundColor Green
    Write-Host "💡 Vous pouvez maintenant faire :" -ForegroundColor Cyan
    Write-Host "   cd $NewName" -ForegroundColor White
    Write-Host "   git remote set-url origin <nouvelle-url>" -ForegroundColor White
} else {
    Write-Host "❌ Le répertoire '$OldName' n'existe pas !" -ForegroundColor Red
    exit 1
}
