<template>
  <label class="label" :for="id"><slot></slot></label>
</template>

<script>
import { defineComponent } from "vue";

export default defineComponent({
  name: "Label",
  props: {
    id: {
      type: String,
      required: false,
    },
  },
  setup() {},
});
</script>

<style>
*label {
  font-family: Arial, Helvetica, sans-serif;
}
</style>
