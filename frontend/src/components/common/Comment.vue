<template>
  <div class="box">
    <section>
      <article>
        <div id="name">{{ name }}</div>
        <div id="time">
          {{ time.getHours() }}:{{ time.getMinutes() }} -
          {{ time.getDate() }}/{{ time.getMonth() + 1 }}/{{
            time.getFullYear()
          }}
        </div>
      </article>
      <article>
        <fieldset id="text">{{ text }}</fieldset>
      </article>
    </section>
  </div>
</template>

<script>
export default {
  name: "Comment",

  props: {
    name: {
      type: String,
      required: true,
    },
    text: {
      type: String,
      required: true,
    },
    time: {
      type: Date,
      required: true,
    },
  },
};
</script>

<style scoped>
article {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  padding: 0.5rem;
}
#name {
  font-size: large;
}
</style>
