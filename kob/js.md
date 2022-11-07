## [声明变量](https://developer.mozilla.org/zh-CN/docs/Learn/JavaScript/First_steps/Variables#声明变量)

要想使用变量，你需要做的第一步就是创建它 -- 更准确的说，是声明一个变量。声明一个变量的语法是在 `var` 或 `let` 关键字之后加上这个变量的名字：

```
let myName;
let myAge;
```



# export

在创建 JavaScript 模块时，**`export`** 语句用于从模块中导出实时绑定的函数、对象或原始值，以便其他程序可以通过 [`import`](https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Statements/import) 语句使用它们。被导出的绑定值依然可以在本地进行修改。在使用 import 进行导入时，这些绑定值只能被导入模块所读取，但在 export 导出模块中对这些绑定值进行修改，所修改的值也会实时地更新。

无论您是否声明，导出的模块都处于[`严格模式`](https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Strict_mode)。export 语句不能用在嵌入式脚本中

## [语法](https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Statements/export#语法)

存在两种 exports 导出方式：

1. 命名导出（每个模块包含任意数量）
2. 默认导出（每个模块包含一个）

# Array.prototype.push()

**`push()`** 方法将一个或多个元素添加到数组的末尾，并返回该数组的新长度。

# this

与其他语言相比，**函数的 `this` 关键字**在 JavaScript 中的表现略有不同，此外，在[严格模式](https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Strict_mode)和非严格模式之间也会有一些差别。

在绝大多数情况下，函数的调用方式决定了 `this` 的值（运行时绑定）。`this` 不能在执行期间被赋值，并且在每次函数被调用时 `this` 的值也可能会不同。ES5 引入了 [bind](https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Global_Objects/Function/bind) 方法来设置函数的 `this` 值，而不用考虑函数如何被调用的。ES2015 引入了[箭头函数](https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Functions/Arrow_functions)，箭头函数不提供自身的 this 绑定（`this` 的值将保持为闭合词法上下文的值）。

### [全局上下文](https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Operators/this#全局上下文)

无论是否在严格模式下，在全局执行环境中（在任何函数体外部）`this` 都指向全局对象。

### [函数上下文](https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Operators/this#函数上下文)

在函数内部，`this`的值取决于函数被调用的方式。

因为下面的代码不在严格模式下，且 `this` 的值不是由该调用设置的，所以 `this` 的值默认指向全局对象，浏览器中就是 [`window`](https://developer.mozilla.org/zh-CN/docs/Web/API/Window)。

**警告：** 在调用 `super()` 之前引用 `this` 会抛出错误。

### [箭头函数](https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Operators/this#箭头函数)

在[箭头函数](https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Functions/Arrow_functions)中，`this`与封闭词法环境的`this`保持一致。在全局代码中，它将被设置为全局对象：

```
var globalObject = this;
var foo = (() => this);
console.log(foo() === globalObject); // true
```

# 箭头函数

**箭头函数表达式**的语法比[函数表达式](https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Operators/function)更简洁，并且没有自己的[`this`](https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Operators/this)，[`arguments`](https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Functions/arguments)，[`super`](https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Operators/super)或[`new.target`](https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Operators/new.target)。箭头函数表达式更适用于那些本来需要匿名函数的地方，并且它不能用作构造函数。

引入箭头函数有两个方面的作用：更简短的函数并且不绑定`this`。

# window.requestAnimationFrame

**`window.requestAnimationFrame()`** 告诉浏览器——你希望执行一个动画，并且要求浏览器在下次重绘之前调用指定的回调函数更新动画。该方法需要传入一个回调函数作为参数，该回调函数会在浏览器下一次重绘之前执行

```
window.requestAnimationFrame(callback);
```

