<template>
  <Button v-on:click="toggleModal">
    <slot name="btnTitle"></slot>
  </Button>

  <div class="modal" :class="{ 'is-active': showModal }">
    <div class="modal-background"></div>
    <div class="modal-content has-background-white py-5 px-5">
      <slot />
      <Button @click="toggleModal"> &#10006; </Button>
      <button
        @click="toggleModal"
        class="modal-close is-large"
        aria-label="close"
      ></button>
    </div>
  </div>
</template>

<script>
import Button from "@/components/common/Button";
import { ref } from "vue";

export default {
  name: "Modal",
  components: { Button },
  setup() {
    const showModal = ref(false);
    const toggleModal = () => {
      showModal.value = !showModal.value;
    };

    return { showModal, toggleModal };
  },
};
</script>

<style scoped>
</style>
