param(
    [Alias("OutputRoot")]
    [string]$UnusedOutputRoot
)

Add-Type -AssemblyName System.Drawing

$color = @{
    Clear = [System.Drawing.Color]::Transparent
    Ink = [System.Drawing.Color]::FromArgb(255, 24, 30, 37)
    Shadow = [System.Drawing.Color]::FromArgb(255, 39, 48, 57)
    Steel = [System.Drawing.Color]::FromArgb(255, 72, 86, 98)
    Light = [System.Drawing.Color]::FromArgb(255, 119, 137, 148)
    Shine = [System.Drawing.Color]::FromArgb(255, 173, 190, 196)
    Panel = [System.Drawing.Color]::FromArgb(255, 18, 39, 45)
    CyanDark = [System.Drawing.Color]::FromArgb(255, 0, 98, 112)
    Cyan = [System.Drawing.Color]::FromArgb(255, 0, 209, 221)
    CyanLight = [System.Drawing.Color]::FromArgb(255, 151, 255, 246)
    Glow = [System.Drawing.Color]::FromArgb(255, 225, 255, 252)
    CopperDark = [System.Drawing.Color]::FromArgb(255, 105, 51, 29)
    Copper = [System.Drawing.Color]::FromArgb(255, 205, 103, 45)
    TinDark = [System.Drawing.Color]::FromArgb(255, 91, 112, 119)
    Tin = [System.Drawing.Color]::FromArgb(255, 154, 181, 184)
    NickelDark = [System.Drawing.Color]::FromArgb(255, 83, 91, 72)
    Nickel = [System.Drawing.Color]::FromArgb(255, 151, 158, 116)
    Amber = [System.Drawing.Color]::FromArgb(255, 255, 190, 61)
    RedDark = [System.Drawing.Color]::FromArgb(255, 99, 34, 30)
    Red = [System.Drawing.Color]::FromArgb(255, 205, 67, 43)
    Graphite = [System.Drawing.Color]::FromArgb(255, 51, 53, 54)
    GuiBack = [System.Drawing.Color]::FromArgb(255, 207, 211, 211)
    GuiLight = [System.Drawing.Color]::FromArgb(255, 241, 243, 240)
    GuiMid = [System.Drawing.Color]::FromArgb(255, 158, 165, 165)
    GuiDark = [System.Drawing.Color]::FromArgb(255, 75, 82, 84)
    Slot = [System.Drawing.Color]::FromArgb(255, 44, 50, 52)
}

function Set-Rectangle($Image, [int]$X, [int]$Y, [int]$Width, [int]$Height, [System.Drawing.Color]$Color) {
    for ($py = $Y; $py -lt $Y + $Height; $py++) {
        for ($px = $X; $px -lt $X + $Width; $px++) {
            $Image.SetPixel($px, $py, $Color)
        }
    }
}

function Set-Points($Image, [System.Drawing.Color]$Color, [object[]]$Points) {
    foreach ($point in $Points) {
        $Image.SetPixel([int]$point[0], [int]$point[1], $Color)
    }
}

function New-Bitmap([int]$Width, [int]$Height, [System.Drawing.Color]$Background) {
    $image = [System.Drawing.Bitmap]::new($Width, $Height, [System.Drawing.Imaging.PixelFormat]::Format32bppArgb)
    Set-Rectangle $image 0 0 $Width $Height $Background
    return $image
}

function New-Texture([System.Drawing.Color]$Background) {
    return New-Bitmap 16 16 $Background
}

function Save-Texture($Image, [string]$Path) {
    $parent = Split-Path -Parent $Path
    New-Item -ItemType Directory -Force -Path $parent | Out-Null
    $Image.Save($Path, [System.Drawing.Imaging.ImageFormat]::Png)
    $Image.Dispose()
}

function Add-CasingFrame($Image, [System.Drawing.Color]$Base, [System.Drawing.Color]$Highlight, [System.Drawing.Color]$Lowlight) {
    Set-Rectangle $Image 0 0 16 16 $color.Ink
    Set-Rectangle $Image 1 1 14 14 $Base
    Set-Rectangle $Image 1 1 14 1 $Highlight
    Set-Rectangle $Image 1 2 1 13 $Highlight
    Set-Rectangle $Image 2 14 13 1 $Lowlight
    Set-Rectangle $Image 14 2 1 13 $Lowlight
    Set-Points $Image $color.Ink @(@(2,2),@(13,2),@(2,13),@(13,13))
}

function Add-GuiFrame($Image, [System.Drawing.Color]$Accent) {
    Set-Rectangle $Image 0 0 176 166 $color.GuiDark
    Set-Rectangle $Image 1 1 174 164 $color.GuiBack
    Set-Rectangle $Image 2 2 172 1 $color.GuiLight
    Set-Rectangle $Image 2 3 1 161 $color.GuiLight
    Set-Rectangle $Image 3 164 171 1 $color.Shadow
    Set-Rectangle $Image 174 3 1 161 $color.Shadow
    Set-Rectangle $Image 3 3 171 2 $Accent
    Set-Rectangle $Image 4 29 167 1 $color.GuiMid
    Set-Rectangle $Image 4 80 167 1 $color.GuiMid
}

function Add-GuiSlot($Image, [int]$X, [int]$Y) {
    Set-Rectangle $Image $X $Y 18 18 $color.GuiDark
    Set-Rectangle $Image ($X + 1) ($Y + 1) 16 16 $color.Slot
    Set-Rectangle $Image ($X + 2) ($Y + 2) 15 1 $color.Shadow
    Set-Rectangle $Image ($X + 2) ($Y + 3) 1 14 $color.Shadow
    Set-Rectangle $Image ($X + 2) ($Y + 16) 15 1 $color.GuiLight
    Set-Rectangle $Image ($X + 16) ($Y + 2) 1 15 $color.GuiLight
}

function Add-GuiInset($Image, [int]$X, [int]$Y, [int]$Width, [int]$Height) {
    Set-Rectangle $Image $X $Y $Width $Height $color.GuiDark
    Set-Rectangle $Image ($X + 1) ($Y + 1) ($Width - 2) ($Height - 2) $color.Slot
    Set-Rectangle $Image ($X + 1) ($Y + $Height - 1) ($Width - 1) 1 $color.GuiLight
    Set-Rectangle $Image ($X + $Width - 1) ($Y + 1) 1 ($Height - 1) $color.GuiLight
}
