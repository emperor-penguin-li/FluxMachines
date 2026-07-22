param(
    [string]$OutputRoot = (Join-Path $PSScriptRoot "../../build/generated/resources/main/assets/fluxmachines/textures")
)

. (Join-Path $PSScriptRoot "00-texture-library.ps1") -OutputRoot $OutputRoot
$guiDir = Join-Path $OutputRoot "gui"

function Add-InventorySlots($Image) {
    for ($row = 0; $row -lt 3; $row++) {
        for ($column = 0; $column -lt 9; $column++) {
            Add-GuiSlot $Image (7 + $column * 18) (83 + $row * 18)
        }
    }
    for ($column = 0; $column -lt 9; $column++) {
        Add-GuiSlot $Image (7 + $column * 18) 141
    }
}

function New-MachineGui([System.Drawing.Color]$Accent, [int]$InputCount, [string]$Kind) {
    $image = New-Bitmap 176 166 $color.GuiBack
    Add-GuiFrame $image $Accent

    $startX = if ($InputCount -eq 1) { 43 } else { 25 }
    for ($slot = 0; $slot -lt $InputCount; $slot++) {
        Add-GuiSlot $image ($startX + $slot * 18) 34
    }
    Add-GuiSlot $image 115 34
    Add-GuiInset $image 83 38 26 10
    Add-GuiInset $image 152 22 12 56
    Add-InventorySlots $image

    # Small machine-specific process glyph beside the progress channel.
    if ($Kind -eq "pulverizer") {
        Set-Points $image $Accent @(@(75,37),@(77,37),@(79,37),@(76,39),@(78,39),@(75,41),@(77,41),@(79,41),@(76,43),@(78,43),@(75,45),@(77,45),@(79,45))
    } elseif ($Kind -eq "wire_mill") {
        Set-Rectangle $image 74 37 3 10 $color.CopperDark
        Set-Rectangle $image 79 37 3 10 $color.CopperDark
        Set-Rectangle $image 75 38 1 8 $Accent
        Set-Rectangle $image 80 38 1 8 $Accent
    }
    return $image
}

$pulverizer = New-MachineGui ([System.Drawing.Color]::FromArgb(255,73,175,196)) 1 "pulverizer"
Save-Texture $pulverizer (Join-Path $guiDir "pulverizer.png")

$wireMill = New-MachineGui ([System.Drawing.Color]::FromArgb(255,213,138,50)) 1 "wire_mill"
Save-Texture $wireMill (Join-Path $guiDir "wire_mill.png")

$alloyFurnace = New-MachineGui ([System.Drawing.Color]::FromArgb(255,226,90,50)) 3 "alloy_furnace"
Save-Texture $alloyFurnace (Join-Path $guiDir "alloy_furnace.png")

$accelerator = New-Bitmap 176 166 $color.GuiBack
Add-GuiFrame $accelerator $color.Cyan
Set-Rectangle $accelerator 7 30 162 52 $color.GuiMid
Set-Rectangle $accelerator 8 31 160 50 $color.GuiLight
Set-Rectangle $accelerator 9 32 158 48 $color.GuiBack
Set-Rectangle $accelerator 7 89 162 34 $color.GuiMid
Set-Rectangle $accelerator 8 90 160 32 $color.GuiLight
Set-Rectangle $accelerator 9 91 158 30 $color.GuiBack
for ($row = 0; $row -lt 3; $row++) {
    for ($column = 0; $column -lt 4; $column++) {
        $x = 128 + $column * 8
        $y = 39 + $row * 8
        Set-Rectangle $accelerator $x $y 5 5 $color.Slot
        Set-Rectangle $accelerator ($x + 1) ($y + 1) 3 3 $color.CyanDark
    }
}
Set-Rectangle $accelerator 7 129 162 1 $color.GuiMid
Set-Rectangle $accelerator 8 137 152 2 $color.CyanDark
Set-Rectangle $accelerator 8 141 116 2 $color.CopperDark
Set-Rectangle $accelerator 8 145 140 2 $color.CyanDark
Set-Rectangle $accelerator 8 149 90 2 $color.CopperDark
Save-Texture $accelerator (Join-Path $guiDir "accelerator.png")

Write-Host "Generated GUI textures in $guiDir"
