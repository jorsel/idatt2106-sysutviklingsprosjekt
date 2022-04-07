<template>
  <section id="wrap">
    <article>
      <h1>Her ser du de 25 sykeste jævlene!</h1>
      <p v-if="!isPublic">
        (Du vises ikke på listen siden din profil er satt til privat)
      </p>
    </article>
    <table class="table">
      <thead>
        <tr>
          <th>Navn</th>
          <th>Intensitet</th>
          <th>Poeng</th>
          <th>Posisjon</th>
        </tr>
      </thead>
      <tfoot>
        <tr>
          <th>Navn</th>
          <th>Intensitet</th>
          <th>Poeng</th>
          <th>Posisjon</th>
        </tr>
      </tfoot>
      <tbody>
        <tr v-for="(entry, index) in boardEntries" :key="index">
          <BoardEntry
            :name="entry.firstName"
            :intensity="entry.intensity"
            :points="entry.points"
            :position="index + 1"
            :isYou="entry.email == yourEmail"
          />
        </tr>
      </tbody>
    </table>
  </section>
</template>

<script>
import BoardEntry from "@/views/leaderboard/BoardEntry.vue";
import { onMounted, ref } from "vue";
import { GET, baseURL, request } from "@/utility/request.js";

export default {
  name: "Leaderboard",

  components: { BoardEntry },

  setup() {
    const boardEntries = ref([]);

    const yourEmail = ref("");

    const isPublic = ref(false);

    onMounted(async () => {
      const email = window.localStorage.getItem("email");
      if (email === null || email === "null") {
        yourEmail.value = "";
      } else {
        yourEmail.value = email;
      }

      isPublic.value = window.localStorage.getItem("public") == "true";

      const response = await request(`${baseURL}/leaderboard`, GET);

      if (response.status === 200) {
        boardEntries.value = response.data;
      } else {
        console.error("An error occured while fetching users!");
      }
    });

    return {
      boardEntries,
      yourEmail,
      isPublic,
    };
  },
};
</script>

<style scoped>
#wrap {
  flex-direction: column;
  display: flex;
  gap: 2rem;
  margin-left: auto;
  margin-right: auto;
}
</style>
