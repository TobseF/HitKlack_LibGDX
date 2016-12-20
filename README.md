# Hit Klack
Retro console game remake of Mephisto Hit Klack

This is a just for fun work in progress game remake. It's based on a German console, the Hit Klack from Mephisto. It runs on multiple platforms: Android, Windows, Linux, and Mac. For me it's a way to test the latest features of [Kotlin](https://kotlinlang.org/), a programming language by [Jetbrains](https://www.jetbrains.com/). It uses the [LibGDX](https://libgdx.badlogicgames.com/) Java game development framework.

## [Official Website and Downloads ...](https://tobsef.github.io/HitKlack/)


## Kotlin Coding Hitlist
Here is a list of nice noticeable Kotlin features which are already in use for HitKlack. Some of them are only available in the newest [EAP of Kotlin 1.1](https://blog.jetbrains.com/kotlin/2016/08/calling-on-eapers/). For a better understanding it may be help to follow direct into the sources.

Model cheatsheet: `GameField` consist out of ten `Ring`s. A Ring contains four `Block`s which each can hold one `Stone`. 

### [Extension Function](https://kotlinlang.org/docs/reference/extensions.html) + [When Expression](https://kotlinlang.org/docs/reference/control-flow.html#when-expression)
``` kotlin
fun Orientation.toControl(): Controller.Control =
  when (this) {
      Orientation.Left -> Controller.Control.Left
      Orientation.Right -> Controller.Control.Right
      Orientation.Up -> Controller.Control.Top
      Orientation.Down -> Controller.Control.Bottom
  }
```
This adds the `toControl()` function to the `Orientation` class as extension. It maps an `Orientation` enum (`Left`,`Right`,`Up`,`Down`) to the Orientation enum (`Left`,`Right`,`Top`,`Bottom`). So we can write `val control = block.orientation.toControl()`.

### [Lamdas and Method References](https://kotlinlang.org/docs/reference/lambdas.html)
``` kotlin
private fun firstFull() = gameField.find(Ring::isFull)

private fun renderField() {
	game.getStones().forEach(renderer::renderStone)
}

private fun getTouchPointers() = (0..6).filter(input::isTouched)
										.map { viewport.unproject(TouchPoint(it)) }
										.filter { !it.isZero }
```
This demonstrates two different types of method references. The `Ring::isFull` points to the `isFull`of the `Iterable<Ring>`. Hopefuly `Ring` can be omitted due a smart compiler. 
In the second example the `renderer::renderStone` points a function of the `renderer`. That’s called a **bound reference** which points to it’s reviever.
The last example, the `getTouchPointers()`, shows the combination of ranges, lamdas, Kotlins stream API and method references. It returns a list of all unprojected touchpoints which are not zero (0, 0).

## [Delegation](https://kotlinlang.org/docs/reference/delegation.html#delegation)
``` kotlin
class Controller(point: Point, val viewport: Viewport) : InputProcessor by InputAdapter(), Point by point {
    //..
}
```
This class implements the `InputProcessor` and the `Point` interface. Callers will be delegated to the implementation declared with the `by` statement.
In pure Java I had to determine one parent because only a simple 1:1 inheritance is possible. And tying to implement both would result in a bunch of boilerplate code.
With the power of delegations this predicament can be solved with a minimum and clear syntax.  

###  [Data classes](https://kotlinlang.org/docs/reference/data-classes.html)
``` kotlin
data class Resolution(var width: Float, var height: Float) {
	fun getCenter() = Point2D(width / 2, height / 2)
}
```
Kotlin automatically generates the `equals()`, `hashCode()`, `toString()` and even a `copy()` for our **data** class. How kindly, isn’t it? GitHub will miss hundreds of hand written boilerplate code in Kotlin project.

### [Type Aliases](https://blog.jetbrains.com/kotlin/2016/07/first-glimpse-of-kotlin-1-1-coroutines-type-aliases-and-more/)
``` kotlin
typealias TexturePair = Pair<TextureRegion, TextureRegion>

private val red: TexturePair
private val blue: TexturePair
private val yellow: TexturePair
private val green: TexturePair

init {
  green = Pair(buttons[0], buttons[1])
  blue = Pair(buttons[2], buttons[3])
  yellow = Pair(buttons[4], buttons[5])
  red = Pair(buttons[6], buttons[7])
}
```
The `Pair` is used as alias of `Pair<TextureRegion, TextureRegion>`. So it’s possible to define a scope where a succinct type name can be used. This reduces boilerplate and can enhance the readability.

### [Inline Functions](https://kotlinlang.org/docs/reference/inline-functions.html)
``` kotlin
fun render(controller: Controller) {
  val radius = width / 2F
  fun draw(textureRegion: TextureRegion, touchArea: Controller.TouchArea) {
  	val pos = touchArea.rect.getCenter(Vector2()).sub(radius, radius)
  	batch.draw(textureRegion, pos.x, pos.y)
  }

  fun button(button: TexturePair, control: Control): TextureRegion {
  	return if (controller.isPressed(control)) button.second else button.first
  }

  draw(button(red, Control.Left), controller.touchAreas[0])
  draw(button(blue, Control.Right), controller.touchAreas[1])
  draw(button(yellow, Control.Bottom), controller.touchAreas[3])
  draw(button(green, Control.Top), controller.touchAreas[2])
}
```
Inline functions give us the freedom to place functions in the scope where they're really needed: Often inside another function. This offers a better way for a meaningful encapsulation.

### [String Templates](https://kotlinlang.org/docs/reference/basic-types.html#string-templates)
``` kotlin
override fun toString() =  "Block [$row ${orientation.char()} $stone]"
``` 
Get rid of the awful and illegible String concatenation and use it’s template feature. Besides that you can see that’s possible to write short funtions into one line without any brackets.

### [Operator Overloading](https://kotlinlang.org/docs/reference/operator-overloading.html)
``` kotlin
val next = geameField[block.row - 1][block.orientation]
```
It looks like an elagant array acces. `field[block.row - 1]` returns a `Ring` and the `[block.orientation]` retunrs the `Array<Block>`. Possible doe the two methods:
``` kotlin
operator fun get(orientation: Orientation) = //[...]
operator fun get(index: Int) = //[...]
```

### [Null Safety](https://kotlinlang.org/docs/reference/null-safety.html#null-safety) and [Elvis Operator](https://kotlinlang.org/docs/reference/null-safety.html#elvis-operator)
``` kotlin
private var activeRing: Ring? = null

private fun resetRing() {
	activeRing?.reset()
}

private fun randomFreeOrientation(): Orientation = activeRing?.randomFreeSide() ?: Orientation.random()
```
With the `?` we can safly call `reset()`. Trying to acces without this check is even forbitten by the compiler:
`Only safe (?.) or non-null asserted (!!.) calls are allowed on nullable reciever of type Ring?`

The second method `randomFreeOrientation()` demonstrates the use case for the Elvis Operator (`?:`). The right side will be only elevated and returned, if the left side was `null`.

With this null safety, it’s clear if a Kotlin type can be nullable. And the compiler does its best to block us from doing unsafe operations. So welcome to world in peace without nullpointer exceptions. NPE RIP




