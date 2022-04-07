<template>
  <div v-if="hasSlots" class="field">
    <p class="control has-icons-left">
      <input
        class="input"
        :id="id"
        :type="type"
        :required="required"
        :name="name"
        :placeholder="placeholder"
        :value="content"
        :modelValue="content"
        @input="inputHandler"
      />
      <span class="icon is-small is-left">
        <slot name="icon"></slot>
      </span>
    </p>
  </div>

  <div v-else class="field">
    <p class="control">
      <input
        :id="id"
        :type="type"
        :required="required"
        :name="name"
        :placeholder="placeholder"
        :value="content"
        :checked="content"
        :modelValue="content"
        @input="inputHandler"
      />
    </p>
  </div>
</template>

<script>
import { defineComponent, ref, computed, onMounted } from "vue";

export default defineComponent({
  name: "Input",
  props: {
    id: {
      type: String,
      required: true,
    },
    type: {
      type: String,
      required: false,
      default: "text",
    },
    placeholder: {
      type: String,
      required: false,
      default: "",
    },
    required: {
      type: Boolean,
      required: false,
      default: false,
    },
    name: {
      type: String,
      required: false,
    },
    value: {
      required: false,
    },
    checked: {
      // type: Boolean,
      required: false,
    },
  },
  setup(props, { emit, slots }) {
    const content = ref(props.value);

    onMounted(() => {
      if (props.type === "checkbox") {
        if (props.checked) {
          content.value = props.checked;
        } else if (props.value === true || props.value === "true") {
          content.value = true;
        } else {
          content.value = false;
        }
      }
    });

    const hasSlots = computed(() => {
      return slots.icon !== undefined;
    });

    const inputHandler = (event) => {
      if (props.type === "radio") {
        emit("update:radio", props.value);
      } else if (props.type === "checkbox") {
        const ret = event.target.checked;
        content.value = event.target.checked;
        emit("update:modelValue", ret);
      } else {
        content.value = event.target.value;
        emit("update:modelValue", event.target.value);
      }
    };

    return { content, inputHandler, hasSlots };
  },
});
</script>

<style></style>
