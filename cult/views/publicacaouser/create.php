<?php

use yii\helpers\Html;


/* @var $this yii\web\View */
/* @var $model app\models\Publicacaouser */

$this->title = 'Adicionar pub. de usuário';
$this->params['breadcrumbs'][] = ['label' => 'Publicação de usuário', 'url' => ['index']];
$this->params['breadcrumbs'][] = $this->title;
?>
<div class="publicacaouser-create">

    <h1><?= Html::encode($this->title) ?></h1>

    <?= $this->render('_form', [
        'model' => $model,
    ]) ?>

</div>
