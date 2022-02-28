## 3.2.0

* Added new constraint pattern enum: `UnsupportedConstraintPattern`.
  * It uses for specify for too complicated or unsupported constraint policy. 

## 3.1.2

* Fix incorrect scope when importing `guava`.

## 3.1.1

* Fix `SemVer.parse(String)` throw `IllegalStateException` when importing this dependency.

## 3.1.0

* Assign `provide` scope to dependencies which doesn't need outside this package
* Add interface `SemVerRangeCollection` for fully expand whatever `Collection` base you wanted.

## 3.0.0

* Simplify range
* Allow parse constraint via abstract class

## 2.0.1

* Typo fix in `SemVerDetermineInRange` (missed `n`)

## 2.0.0

* Use `Function` to make condition on constraint pattern

## 1.0.1

* Add inspection year

## 1.0.0

Initial release