<template>
  <button class="button button_red" :id="id" :type="type"><slot></slot></button>
</template>

<script>
import { defineComponent } from "vue";

export default defineComponent({
  name: "Button",
  props: {
    id: {
      type: String,
      required: false,
    },
    type: {
      type: String,
      required: false,
      default: "button",
    },
  },
  setup() {},
});
</script>

<style>
.button_red {
  background-color: #ed1c24;
  color: #fff;
  /* padding: 2px;
  margin: 0.5rem; */
}
</style>
