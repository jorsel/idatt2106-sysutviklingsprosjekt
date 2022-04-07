<template>
  <div class="popup">
    <div class="popup-inner">
      <slot />
      <br />
      <br />
      <Button
        @click="
          confirmation();
          goBack();
        "
      >
        Ja
      </Button>
      <Button @click="goBack"> Nei </Button>
    </div>
  </div>
</template>


<script>
import Button from "@/components/common/Button";

export default {
  name: "Confirmation",

  components: { Button },

  props: {
    goBack: {
      type: Function,
      required: true,
    },
    confirmation: {
      type: Function,
      required: true,
    },
  },
};
</script>




<style  scoped>
.popup {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 99;
  background-color: rgba(0, 0, 0, 0.2);

  display: flex;
  align-items: center;
  justify-content: center;
}

.popup-inner {
  background: #fff;
  padding: 32px;
}
</style>
