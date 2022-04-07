<template>
  <div v-if="hasSlots" class="control has-icons-left">
    <div class="select">
      <select
        :modelValue="content"
        :value="content"
        @input="inputHandler"
        :id="id"
        :required="required"
      >
        <option v-for="(opt, index) in options" :key="index" :value="opt.value">
          {{ opt.key }}
        </option>
      </select>
    </div>
    <div class="icon is-small is-left">
      <slot name="icon"></slot>
    </div>
  </div>

  <div v-else class="select">
    <select
      :modelValue="content"
      @input="inputHandler"
      :id="id"
      :required="required"
    >
      <option v-for="(opt, index) in options" :key="index" :value="opt.value">
        {{ opt.key }}
      </option>
    </select>
  </div>
</template>

<script>
import { ref, defineComponent, computed } from "vue";

export default defineComponent({
  name: "Select",
  props: {
    id: {
      type: String,
      required: false,
    },
    required: {
      type: Boolean,
      required: false,
      default: false,
    },
    options: {
      type: Array,
      required: true,
    },
    value: {
      required: false,
    },
  },
  setup(props, { emit, slots }) {
    const content = ref(props.value);
    const hasSlots = computed(() => {
      return slots.icon !== undefined;
    });

    const inputHandler = (event) => {
      content.value = event.target.value;
      emit("update:modelValue", event.target.value);
    };

    return { content, inputHandler, hasSlots };
  },
});
</script>

<style></style>
