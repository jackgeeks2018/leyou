<template>
  <v-form v-model="valid" ref="brandFrom">
    <v-text-field label="品牌名称" v-model="brand.name" required :rules="[v => !!v || '品牌名称不能为空']" />
    <v-text-field label="首字母" v-model="brand.letter" required mask="A" :rules="letterRules" />
    <v-cascader
      url="/item/category/list"
      multiple
      required
      v-model="brand.categories"
      label="请选择商品分类"
    />
    <v-layout row>
      <v-flex xs3>
        <span style="font-size: 16px; color: #444">品牌LOGO：</span>
      </v-flex>
      <v-flex>
        <v-upload
          v-model="brand.image"
          url="/item/upload/image"
          :multiple="false"
          :pic-width="250"
          :pic-height="90"
        />
      </v-flex>
    </v-layout>
    <v-layout class="my-4">
      <v-btn @click="submit" color="primary">提交</v-btn>
      <v-btn @click="clear" color="warning">重置</v-btn>
    </v-layout>
  </v-form>
</template>

<script>
export default {
  name: "my-brand-form",
  props: {
    oldBrand: Object,
    isEdit: {
      type: Boolean,
      default: false
    },
    show: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      valid: false,
      brand: {
        name: "", // 品牌名称
        letter: "", // 品牌首字母
        image: "", // 品牌logo
        categories: [] // 品牌所属的商品分类数组
      },
      nameRules: [
        v => !!v || "品牌名称不能为空",
        v => v.length > 1 || "品牌名称至少2位"
      ],
      letterRules: [
        v => !!v || "首字母不能为空",
        v => /^[A-Z]{1}$/.test(v) || "品牌字母只能是A~Z的大写字母"
      ]
    };
  },
  watch: {
    oldBrand: {
      // 监控oldBrand的变化
      handler(val) {
        if (val) {
          // 注意不要直接复制，否则这边的修改会影响到父组件的数据，copy属性即可
          this.brand = Object.deepCopy(val, this.brand);
        } else {
          // 为空，初始化brand
          this.brand = {
            name: "",
            letter: "",
            image: "",
            categories: []
          };
        }
      },
      deep: true
    }
  },
  methods: {
    submit() {
      if (this.$refs.brandFrom.validate()) {
        // 2、定义一个请求参数对象，通过解构表达式来获取brand中的属性
        const { categories, letter, ...params } = this.brand;
        // 3、数据库中只要保存分类的id即可，因此我们对categories的值进行处理,只保留id，并转为字符串
        params.cids = categories.map(c => c.id).join(",");
        // 4、将字母都处理为大写
        params.letter = letter.toUpperCase();
        // 将数据提交到后台
        this.$http({
          method: this.isEdit ? "put" : "post", // 动态判断是POST还是PUT
          url: "/item/brand",
          data: this.$qs.stringify(params)
        }) .then(() => {
            //console.log(this)
            // 关闭窗口
            this.$emit("close");
            this.$message.success("保存成功！");
            //this.closeWindow();
          })
          .catch(e => {
            console.log(e);
            this.$message.error("保存失败！");
          });
      }
    },
    clear() {
      // 重置表单
      this.$refs.brandFrom.reset();
      // 需要手动清空商品分类
      this.categories = [];
    }
  }
};
</script>

<style scoped>
</style>
