# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),

## [1.4.1] - 2023-02-02

### Fixed

- Fixed UI overlap if in a claimed chunk while using FTB Chunks

## [1.4.0] - 2023-01-30

### Added

- Added optional support for CuriosAPI
  - If CuriosAPI is loaded, then the Calendar item from SereneSeason can be equipped in the "Charm" slot.
  - If the "Need Calendar" option is enabled, then a Calendar in the "Charm" slot will meet the requirement

## [1.3.6] - 2023-01-21

### Changed
- Some code cleanup

## [1.3.5] - 2023-01-20

### Added

- Added support for Xaero's Minimap Fair-play

## [1.3.4] - 2023-01-19

### Added

- Added an option to show the HUD only if the player has the Calendar item in their inventory

## [1.3.3] - 2023-01-11

### Added

- Added config option with presets for HUD location (when no minimap is present)
- Added button in config screen for HUD location

## [1.3.2] - 2023-01-10

### Added

- Added config options to offset the default HUD

### Changed

- Built against latest recommended Forge version

## [1.3.1] - 2023-01-06

### Added

- Added an option to disable the mod ingame.


### Changed

- Slight code cleanup

## [1.3.0] - 2022-12-29

### Added

- Added am in-game config screen and a keybind for it.
  - Default key is 'H'


### Changed

- Due to some changes from 1.19.2 -> 1.19.3, I had to change to separate builds for each, rather than a unified version
- Fixed an issue with tropical season dates. Should be accurate now.
