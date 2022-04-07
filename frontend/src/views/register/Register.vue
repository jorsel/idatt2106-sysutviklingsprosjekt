<template>
  <div class="box" id="box">
    <section v-if="loggedIn" id="register">
      <p>Du er allerede registrert og pålogget</p>
    </section>

    <section v-else id="register">
      <div class="notification is-info" v-if="msg">
        {{ msg }}
      </div>

      <form @submit.prevent="onSubmit">
        <h3>Fyll inn nødvendig informasjon</h3>
        <div class="field">
          <Label :id="'firstName'">Fornavn:</Label>
          <div class="control">
            <Input
              :type="'text'"
              :id="'firstName'"
              :required="required"
              v-model="user.firstName"
            >
              <template v-slot:icon>
                <em class="fas fa-user"></em>
              </template>
            </Input>
          </div>
        </div>
        <div class="field">
          <Label :id="'lastName'">Etternavn:</Label>
          <div class="control">
            <Input
              :type="'text'"
              :id="'lastName'"
              :required="required"
              v-model="user.lastName"
            >
              <template v-slot:icon>
                <em class="fas fa-user"></em>
              </template>
            </Input>
          </div>
        </div>
        <div class="field">
          <Label :id="'dateOfBirth'">Fødselsdag:</Label>
          <div class="control">
            <v-date-picker v-model="user.dateOfBirth" mode="date" />
          </div>
          <p class="help is-danger" v-show="msgDate">
            {{ msgDate }}
          </p>
        </div>
        <div class="field">
          <Label :id="'email'">E-mail:</Label>
          <div class="control">
            <Input
              :type="'email'"
              :id="'email'"
              :required="required"
              v-model="user.email"
            >
              <template v-slot:icon>
                <em class="fas fa-envelope"></em>
              </template>
            </Input>
          </div>
        </div>
        <div class="field">
          <Label :id="'intensity'">Aktivitestnivå:</Label>
          <div class="control">
            <Select
              :id="'intensity'"
              :required="required"
              :value="1"
              :options="options"
              v-model="user.intensity"
            >
              <template v-slot:icon>
                <em class="fas fa-heartbeat"></em>
              </template>
            </Select>
          </div>
        </div>
        <div class="field">
          <Label :id="'public'">Offentlig konto?:</Label>
          <div class="control">
            <Input
              class="checkbox"
              :type="'checkbox'"
              :id="'public'"
              v-model="user.public"
            />
          </div>
        </div>
        <div class="field">
          <Label :id="'password'">Passord:</Label>
          <div class="control">
            <Input
              :type="'password'"
              :id="'password'"
              :required="required"
              v-model="user.password"
            >
              <template v-slot:icon>
                <em class="fas fa-lock"></em>
              </template>
            </Input>
          </div>
          <p class="help is-danger" v-show="msgPass">
            {{ msgPass }}
          </p>
        </div>
        <div class="field">
          <Label :id="'rePassword'">Gjenta passord:</Label>
          <div class="control">
            <Input
              :type="'password'"
              :id="'rePassword'"
              :required="required"
              v-model="user.rePassword"
            >
              <template v-slot:icon>
                <em class="fas fa-lock"></em>
              </template>
            </Input>
          </div>
        </div>
        <Button :type="'submit'">Register</Button>
      </form>
    </section>
  </div>
</template>

<script>
import { onMounted, ref, reactive } from "vue";
import { useRouter } from "vue-router";

import { POST, baseURL, request } from "@/utility/request.js";

import Button from "@/components/common/Button.vue";
import Input from "@/components/common/Input.vue";
import Label from "@/components/common/Label.vue";
import Select from "@/components/common/Select.vue";

export default {
  name: "Register",
  components: {
    Button,
    Input,
    Label,
    Select,
  },
  setup() {
    const router = useRouter();

    const options = ref([
      { key: "Lav", value: 1 },
      { key: "Medium", value: 2 },
      { key: "Høy", value: 3 },
    ]);

    const required = ref(true);
    const msgPass = ref("");
    const msgDate = ref("");
    const loggedIn = ref(false);
    const msg = ref("");

    const user = reactive({
      firstName: "",
      lastName: "",
      dateOfBirth: null,
      email: "",
      password: "",
      rePassword: "",
      intensity: 0,
      public: false,
    });

    onMounted(() => {
      if (window.localStorage.getItem("firstName")) {
        loggedIn.value = true;
      }
    });

    const onSubmit = async () => {
      if (user.password !== user.rePassword) {
        msgPass.value = "Passordene er ikke like hverandre!";
        return;
      } else if (user.dateOfBirth === null || Date.now() < user.dateOfBirth) {
        msgDate.value = "Fyll inn en gyldig Fødselsdag!";
        return;
      } else {
        msgPass.value = "";
        msgDate.value = "";
      }

      try {
        // delete user.rePassword;
        const response = await request(
          `${baseURL}/register`,
          POST,
          JSON.stringify(user)
        );

        if (response?.status === 200) {
          Object.assign(user, {
            firstName: "",
            lastName: "",
            dateOfBirth: null,
            email: "",
            password: "",
            rePassword: "",
            intensity: 0,
            public: false,
          });

          router.push({
            name: "Login",
            params: { msg: "Du er registrert og kan nå logge inn." },
          });
        } else {
          console.error("An error occured whiled sending login request!");
          msg.value =
            "En feil oppstod og registreringen kunne ikke bli utført!";
        }
      } catch {
        console.error("An error occured whiled sending login request!");
        msg.value = "En feil oppstod og registreringen kunne ikke bli utført!";
      }
    };

    return {
      user,
      required,
      msg,
      options,
      loggedIn,
      msgDate,
      msgPass,
      onSubmit,
    };
  },
};
</script>
<style scoped>
#register {
  background-color: white;
  padding: 10px;
  width: inherit;
  margin-left: auto;
  margin-right: auto;
  display: flex;
  justify-content: center;
  flex-direction: column;
}

#box {
  flex-direction: column;
  display: flex;
  margin-left: auto;
  margin-right: auto;
}

#register * {
  margin-left: auto;
  margin-right: auto;
  position: center;
}
</style>
