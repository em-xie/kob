在 GameMap.vue 中添加 canvas

： == v-bind   `v-on` 指令监听 DOM 事件：@ == v-on

###### 表单绑定 v-model



我们可以使用[响应式 API](https://cn.vuejs.org/api/reactivity-core.html) 来声明响应式的状态，在 `setup()` 函数中返回的对象会暴露给模板和组件实例。其它的选项也可以通过组件实例来获取 `setup()` 暴露的属性：

```
<script>
import { ref } from 'vue'

export default {
  setup() {
    const count = ref(0)

    // 返回值会暴露给模板和其他的选项式 API 钩子
    return {
      count
    }
  },

  mounted() {
    console.log(this.count) // 0
  }
}
</script>

<template>
  <button @click="count++">{{ count }}</button>
</template>

```

请注意在模板中访问从 `setup` 返回的 [ref](https://cn.vuejs.org/api/reactivity-core.html#ref) 时，它会[自动浅层解包](https://cn.vuejs.org/guide/essentials/reactivity-fundamentals.html#deep-reactivity)，因此你无须再在模板中为它写 `.value`。当通过 `this` 访问时也会同样如此解包。

## ref()[#](https://cn.vuejs.org/api/reactivity-core.html#ref)

接受一个内部值，返回一个响应式的、可更改的 ref 对象，此对象只有一个指向其内部值的属性 `.value`。

- **类型**

```
function ref<T>(value: T): Ref<UnwrapRef<T>>

interface Ref<T> {
  value: T
}

```

- **详细信息**

  ref 对象是可更改的，也就是说你可以为 `.value` 赋予新的值。它也是响应式的，即所有对 `.value` 的操作都将被追踪，并且写操作会触发与之相关的副作用。

  如果将一个对象赋值给 ref，那么这个对象将通过 [reactive()](https://cn.vuejs.org/api/reactivity-core.html#reactive) 转为具有深层次响应式的对象。这也意味着如果对象中包含了嵌套的 ref，它们将被深层地解包。

  若要避免这种深层次的转换，请使用 [`shallowRef()`](https://cn.vuejs.org/api/reactivity-advanced.html#shallowref) 来替代。

- **示例**

```
const count = ref(0)
console.log(count.value) // 0

count.value++
console.log(count.value) // 1

```

## onMounted()[#](https://cn.vuejs.org/api/composition-api-lifecycle.html#onmounted)

注册一个回调函数，在组件挂载完成后执行。

- **类型**

  

  ```
  function onMounted(callback: () => void): void
  ```

- **详细信息**

  组件在以下情况下被视为已挂载：

  - 其所有同步子组件都已经被挂载 (不包含异步组件或 `<Suspense>` 树内的组件)。
  - 其自身的 DOM 树已经创建完成并插入了父容器中。注意仅当根容器在文档中时，才可以保证组件 DOM 树也在文档中。

  这个钩子通常用于执行需要访问组件所渲染的 DOM 树相关的副作用，或是在[服务端渲染应用](https://cn.vuejs.org/guide/scaling-up/ssr.html)中用于确保 DOM 相关代码仅在客户端执行。

  **这个钩子在服务器端渲染期间不会被调用。**

- **示例**

  通过模板引用访问一个元素：

```
<script setup>
import { ref, onMounted } from 'vue'

const el = ref()

onMounted(() => {
  el.value // <div>
})
</script>

<template>
  <div ref="el"></div>
</template>

```

